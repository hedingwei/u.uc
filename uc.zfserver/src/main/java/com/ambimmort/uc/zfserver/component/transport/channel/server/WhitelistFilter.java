/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ambimmort.uc.zfserver.component.transport.channel.server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;
import org.apache.mina.filter.firewall.Subnet;
import org.slf4j.LoggerFactory;

/**
 * Whitelist filter (based on Mina's Blacklist filter).
 */
public class WhitelistFilter extends IoFilterAdapter {

	private final List<Subnet> whitelist = new CopyOnWriteArrayList<Subnet>();

	private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(WhitelistFilter.class);

	public void setWhitelist(InetAddress[] addresses) {
		if (addresses == null) {
			throw new NullPointerException("addresses");
		}
		whitelist.clear();
		for (InetAddress addr : addresses) {
			allow(addr);
		}
	}

	public void setSubnetWhitelist(Subnet[] subnets) {
		if (subnets == null) {
			throw new NullPointerException("Subnets must not be null");
		}
		whitelist.clear();
		for (Subnet subnet : subnets) {
			allow(subnet);
		}
	}

	public void setWhitelist(Iterable<InetAddress> addresses) {
		if (addresses == null) {
			throw new NullPointerException("addresses");
		}

		whitelist.clear();

		for (InetAddress address : addresses) {
			allow(address);
		}
	}

	public void setSubnetWhitelist(Iterable<Subnet> subnets) {
		if (subnets == null) {
			throw new NullPointerException("Subnets must not be null");
		}
		whitelist.clear();
		for (Subnet subnet : subnets) {
			allow(subnet);
		}
	}

	public void allow(InetAddress address) {
		if (address == null) {
			throw new NullPointerException("Adress to block can not be null");
		}

		allow(new Subnet(address, 32));
	}

	public void allow(Subnet subnet) {
		if (subnet == null) {
			throw new NullPointerException("Subnet can not be null");
		}

		whitelist.add(subnet);
	}

	public void disallow(InetAddress address) {
		if (address == null) {
			throw new NullPointerException("Adress to unblock can not be null");
		}

		disallow(new Subnet(address, 32));
	}

	public void disallow(Subnet subnet) {
		if (subnet == null) {
			throw new NullPointerException("Subnet can not be null");
		}
		whitelist.remove(subnet);
	}

	@Override
	public void sessionCreated(NextFilter nextFilter, IoSession session) {
		if (isAllowed(session)) {
			nextFilter.sessionCreated(session);
		} else {
			blockSession(session);
		}
	}

	@Override
	public void sessionOpened(NextFilter nextFilter, IoSession session) throws Exception {
		if (isAllowed(session)) {
			nextFilter.sessionOpened(session);
		} else {
			blockSession(session);
		}
	}

	@Override
	public void sessionClosed(NextFilter nextFilter, IoSession session) throws Exception {
		if (isAllowed(session)) {
			nextFilter.sessionClosed(session);
		} else {
			blockSession(session);
		}
	}

	@Override
	public void sessionIdle(NextFilter nextFilter, IoSession session, IdleStatus status) throws Exception {
		if (isAllowed(session)) {
			nextFilter.sessionIdle(session, status);
		} else {
			blockSession(session);
		}
	}

	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session, Object message) {
		if (isAllowed(session)) {
			nextFilter.messageReceived(session, message);
		} else {
			blockSession(session);
		}
	}

	@Override
	public void messageSent(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
		if (isAllowed(session)) {
			nextFilter.messageSent(session, writeRequest);
		} else {
			blockSession(session);
		}
	}

	private void blockSession(IoSession session) {
		LOGGER.warn("Remote address is not allowed; closing.");
		session.close(true);
	}

	private boolean isAllowed(IoSession session) {
		SocketAddress remoteAddress = session.getRemoteAddress();
		if (remoteAddress instanceof InetSocketAddress) {
			InetAddress address = ((InetSocketAddress) remoteAddress).getAddress();

			for (Subnet subnet : whitelist) {
				if (subnet.inSubnet(address)) {
					return true;
				}
			}
		}
		return false;
	}
}
